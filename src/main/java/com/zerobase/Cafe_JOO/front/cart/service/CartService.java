package com.zerobase.Cafe_JOO.front.cart.service;

import com.zerobase.Cafe_JOO.common.config.security.TokenProvider;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.front.cart.domain.Cart;
import com.zerobase.Cafe_JOO.front.cart.domain.CartOption;
import com.zerobase.Cafe_JOO.front.cart.domain.CartOptionRepository;
import com.zerobase.Cafe_JOO.front.cart.domain.CartRepository;
import com.zerobase.Cafe_JOO.front.cart.dto.CartAddForm;
import com.zerobase.Cafe_JOO.front.cart.dto.CartListDto;
import com.zerobase.Cafe_JOO.front.cart.dto.CartListOptionDto;
import com.zerobase.Cafe_JOO.front.cart.dto.CartProductDto;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.member.domain.MemberRepository;
import com.zerobase.Cafe_JOO.front.product.domain.Option;
import com.zerobase.Cafe_JOO.front.product.domain.OptionRepository;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import com.zerobase.Cafe_JOO.front.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.*;
import static com.zerobase.Cafe_JOO.common.type.CartOrderStatus.BEFORE_ORDER;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final CartOptionRepository cartOptionRepository;

    private final ProductRepository productRepository;

    private final OptionRepository optionRepository;

    private final TokenProvider tokenProvider;

    // 장바구니 목록 조회
    public List<CartListDto> findCartList(String token) {
        Long memberId = tokenProvider.getId(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findAllByMemberAndStatus(member, BEFORE_ORDER);

        List<CartListDto> cartListDtoList = new ArrayList<>();

        for (Cart cart : cartList) {
            List<CartOption> allByCart = cartOptionRepository.findAllByCart(cart);

            List<CartListOptionDto> cartListOptionDtos = new ArrayList<>();

            // Entity -> dto 변환.
            for (CartOption cartOption : allByCart) {
                cartListOptionDtos.add(CartListOptionDto.from(cartOption.getOption()));
            }

            cartListDtoList.add(CartListDto.from(cart, cartListOptionDtos));
        }

        return cartListDtoList;
    }

    // 장바구니에 상품 삭제
    public List<CartProductDto> removeCart(String token, CartAddForm cartAddForm) {

        Long userId = tokenProvider.getId(token);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartAddForm.getProductId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findByMemberAndProduct(member,product);

        if (cartList.size() == 0) {
            throw new CustomException(PRODUCT_NOT_EXISTS);
        }

        if (cartList.size() > 0) {

            Boolean result = false; // 중복 카트 여부 변수

            Integer quantity = 0;   // 삭제된 카트의 수를 기록하는 변수

            for (Cart otherCart : cartList) {

                List<Integer> optionIdList = from(otherCart); // otherCart는 중복 체크 기준 변수

                Collections.sort(optionIdList);

                // optionIdCopyList = 정렬된 optionIdList
                List<Integer> optionIdCopyList = cartAddForm.getOptionIdList().stream()
                        .sorted()
                        .collect(Collectors.toList()); // toList() : 새로운 List 객체로 변환

                if (optionIdList.size() == cartAddForm.getOptionIdList().size()) {
                    result = compare(optionIdList, optionIdCopyList);
                    if (result) { // result = true(중복)
                        cartOptionRepository.deleteAllByCart(otherCart); // 해당 카트의 모든 옵션 삭제
                        cartRepository.deleteById(otherCart.getId());    // 해당 카트 삭제
                        quantity++; // 중복 카트가 삭제될 때마다 1씩 증가
                    }
                }
            }
            if (quantity == 0) {
                throw new CustomException(PRODUCT_NOT_EXISTS);
            }
        }
        List<Cart> carts = cartRepository.findByMember(member);

        // CartProductDto 를 List (여러 값) 담을 빈 그릇
        List<CartProductDto> cartProductDtoList = new ArrayList<>();

        for (Cart otherCart : carts) {

            CartProductDto cartProductDto = CartProductDto.from(otherCart);

            List<CartOption> cartOptionList = cartOptionRepository.findByCart(otherCart);

            for (CartOption cartOption : cartOptionList) {
                cartProductDto.addOptionId(cartOption.getOption().getId());
            }

            cartProductDtoList.add(cartProductDto);
        }

        return cartProductDtoList;
    }

    // 장바구니 상품 수량 변경
    public List<CartProductDto> modifyCart(String token, CartAddForm cartAddForm) {

        Long userId = tokenProvider.getId(token);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartAddForm.getProductId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findByMemberAndProduct(member, product);

        if (cartList.size() == 0) {
            throw new CustomException(PRODUCT_NOT_EXISTS);
        }

        if (cartList.size() > 0) {

            Boolean result = false; // 초기 값 설정

            Integer quantity = 0;   // 초기 값 설정

            for (Cart otherCart : cartList) {

                List<Integer> optionIdList = from(otherCart);

                Collections.sort(optionIdList);

                List<Integer> optionIdCopyList = cartAddForm.getOptionIdList().stream()
                        .sorted()
                        .collect(Collectors.toList());

                if (optionIdList.size() == cartAddForm.getOptionIdList().size()) {
                    result = compare(optionIdList, optionIdCopyList);
                    if (result) {
                        otherCart.setQuantity(cartAddForm.getQuantity());
                        cartRepository.save(otherCart);
                        quantity++;
                    }
                }
            }
            if (quantity == 0) {
                throw new CustomException(PRODUCT_NOT_EXISTS);
            }
        }

        List<Cart> carts = cartRepository.findByMember(member);

        List<CartProductDto> cartProductDtoList = new ArrayList<>();

        for (Cart otherCart : carts) {

            // Entity -> dto transfer (use from method)
            CartProductDto cartProductDto = CartProductDto.from(otherCart);

            List<CartOption> cartOptionList = cartOptionRepository.findByCart(otherCart);

            for (CartOption cartOption : cartOptionList) {
                cartProductDto.addOptionId(cartOption.getOption().getId());
            }
            cartProductDtoList.add(cartProductDto);
        }
        return cartProductDtoList;
    }

    // 장바구니에 상품 넣기
    public List<CartProductDto> saveCart(String token, CartAddForm cartAddForm) {

        Long userId = tokenProvider.getId(token);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartAddForm.getProductId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findByMemberAndProduct(member, product);

        if (cartList.size() == 0) {

            Cart cart = Cart.createCart(member, product, cartAddForm.getQuantity(),
                    cartAddForm.getCartOrderStatus());

            cartRepository.save(cart);

            for (Integer optionId : cartAddForm.getOptionIdList()) {

                Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS));

                CartOption cartOption = CartOption.createCartOption(cart, option);

                cartOptionRepository.save(cartOption);
            }
        } else if (cartList.size() > 0){

            Boolean result = false;

            Integer quantity = 0;

            for (Cart otherCart : cartList) {

                List<Integer> optionIdList = from(otherCart);

                Collections.sort(optionIdList);

                List<Integer> optionIdCopyList = cartAddForm.getOptionIdList().stream()
                        .sorted()
                        .collect(Collectors.toList());

                if (optionIdList.size() == cartAddForm.getOptionIdList().size()) ;
                result = compare(optionIdList, optionIdCopyList);

                if (result) {
                    otherCart.addQuantity(cartAddForm.getQuantity());
                    cartRepository.save(otherCart);
                    quantity++;
                }
            }
        }

        
    }

    // Cart 객체를 사용해서 optionId들을 리스트에 저장
    private List<Integer> from(Cart cart) {

        List<CartOption> cartOptionList = cartOptionRepository.findByCart(cart);

        // 빈 그릇
        List<Integer> optionIdList = new ArrayList<>();

        for (CartOption cartOption : cartOptionList) {
            // 빈 그릇에 CartOption의 id를 가져옴
            optionIdList.add(cartOption.getOption().getId());
        }

        // 그 값을 반환 (from(otherCart)에 적용 할 값)
        return optionIdList;
    }

    // optionId 리스트가 동일한지 비교
    private boolean compare(List<Integer> optionIdList1, List<Integer> optionIdList2) {

        String s1 = optionIdList1.toString();

        String s2 = optionIdList2.toString();

        return s1.equals(s2);
    }
}















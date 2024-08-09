package microservice.ecommerce_cart_service.Mappers;

import at.backend.drugstore.microservice.common_models.DTOs.Product.ProductDTO;
import microservice.ecommerce_cart_service.Model.Cart;
import at.backend.drugstore.microservice.common_models.DTOs.Cart.CartDTO;
import at.backend.drugstore.microservice.common_models.DTOs.Cart.CartItemDTO;
import microservice.ecommerce_cart_service.Model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDtoMapper {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartDtoMapper(CartMapper cartMapper, CartItemMapper cartItemMapper) {
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public CartDTO createCartDTO(Cart cart) {
        CartDTO cartDTO = cartMapper.entityToDTO(cart);
        List<CartItemDTO> cartItemDTOS = cart.getCartItems().stream()
                .map(cartItemMapper::entityToDTO)
                .collect(Collectors.toList());
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }

    public CartItem createCartItem(ProductDTO productDTO, int quantity, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productDTO.getId());
        cartItem.setProductName(productDTO.getName());
        cartItem.setProductPrice(productDTO.getPrice());
        cartItem.setQuantity(quantity);
        cartItem.setItemTotal(productDTO.getPrice().multiply(new BigDecimal(quantity)));
        cartItem.setCart(cart);
        return cartItem;
    }
}

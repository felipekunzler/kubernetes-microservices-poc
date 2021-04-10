import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Cart } from '../core/models/cart';
import { CartService } from '../core/services/cart.service';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  constructor(
    private cartService: CartService
  ) {
  }

  cart$!: Observable<Cart>;

  ngOnInit(): void {
    const cartId = localStorage.getItem('cartId');

    if (cartId) {
      this.cart$ = this.cartService.getCart(cartId);
    } else {
      this.cart$ = this.cartService.createEmptyCart()
        .pipe(tap(
          c => localStorage.setItem('cartId', c.id)
        ));
    }
  }

}

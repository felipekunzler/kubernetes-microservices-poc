import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Cart} from '../core/models/cart';
import {CartService} from '../core/services/cart.service';
import {tap} from 'rxjs/operators';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.scss'],
    standalone: false
})
export class CartComponent implements OnInit {

  constructor(
    public cartService: CartService
  ) {
  }

  cart$!: Observable<Cart>;

  ngOnInit(): void {
      this.cart$ = this.cartService.getOrCreateEmptyCart();
  }

  deleteEntry(id?: number): void {
    this.cartService.deleteEntry(id).subscribe(() => {
        this.cart$ = this.cartService.getOrCreateEmptyCart();
    });
  }

  updateQuantity(id?: number, quantity?: number): void {
    if (quantity && id && quantity >= 1 && quantity <= 99) {
      this.cartService.updateQuantity(id, quantity).subscribe(() => {
        this.cart$ = this.cartService.getOrCreateEmptyCart();
      });
    }
  }

}

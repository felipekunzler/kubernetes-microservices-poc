import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Product} from '../core/models/product';
import {ProductService} from '../core/services/product.service';
import {ActivatedRoute} from '@angular/router';
import {CartService} from '../core/services/cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss']
})
export class ProductDetailsComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    public cartService: CartService
  ) {
  }

  quantity = 1;
  product$!: Observable<Product>;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id') || '';
    this.product$ = this.productService.getProduct(id);
  }

  updateQuantity(updatedQuantity: number): void {
    if (updatedQuantity > 0 && updatedQuantity < 99) {
      this.quantity = updatedQuantity;
    }
  }

}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cart } from '../models/cart';
import { HttpClient } from '@angular/common/http';
import { Entry } from '../models/entry';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(
    private http: HttpClient
  ) {
  }

  getCart(id: string): Observable<Cart> {
    return this.http.get<Cart>('http://localhost:4200/api/cart/' + id);
  }

  createEmptyCart(): Observable<Cart> {
    return this.http.post<Cart>('http://localhost:4200/api/cart', null, {});
  }

  addToCart(pId: string): void {
    const newEntry: Entry = {
      productId: pId,
      quantity: 1
    };
    const cartId = localStorage.getItem('cartId');
    this.http.post('http://localhost:4200/api/cart/' + cartId + '/entry', newEntry, {})
      .subscribe();
  }

}

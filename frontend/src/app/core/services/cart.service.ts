import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Cart} from '../models/cart';
import {HttpClient} from '@angular/common/http';
import {Entry} from '../models/entry';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
  }

  getCart(id: string): Observable<Cart> {
    return this.http.get<Cart>('http://localhost:4200/api/cart/' + id);
  }

  createEmptyCart(): Observable<Cart> {
    return this.http.post<Cart>('http://localhost:4200/api/cart', null, {});
  }

  deleteEntry(id?: number): Observable<any> {
    const cartId = localStorage.getItem('cartId');
    return this.http.delete('http://localhost:4200/api/cart/' + cartId + '/entry/' + id);
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

  updateQuantity(id: number, quantity: number): Observable<any> {
    const entry: Entry = {id, quantity};
    const cartId = localStorage.getItem('cartId');
    return this.http.patch('http://localhost:4200/api/cart/' + cartId + '/entry/' + id, entry, {});
  }

  placeOrder(): void {
    const cartId = localStorage.getItem('cartId');
    this.http.post('http://localhost:4200/api/cart/' + cartId + '/placeOrder', null, {})
      .subscribe();
    localStorage.removeItem('cartId');
    this.router.navigate(['']);
  }

}

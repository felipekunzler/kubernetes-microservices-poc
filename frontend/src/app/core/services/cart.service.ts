import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {Cart} from '../models/cart';
import {HttpClient} from '@angular/common/http';
import {Entry} from '../models/entry';
import {Router} from '@angular/router';
import {map, tap} from 'rxjs/operators';
import {SessionService} from './session.service';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private sessionService: SessionService
  ) {
  }

  getOrCreateEmptyCart(): Observable<Cart> {
    const cartId = this.sessionService.getCurrentCartId();
    if (cartId) {
      return this.getCart(cartId);
    }
    return this.createEmptyCart();
  }

  getOrCreateCartId(): Observable<string> {
    const cartId = this.sessionService.getCurrentCartId();
    if (cartId) {
      return of(cartId);
    }
    return this.createEmptyCart().pipe(map(cart => cart.id));
  }

  getCart(id: string): Observable<Cart> {
    return this.http.get<Cart>(environment.cartServiceUrl + '/cart/' + id);
  }

  createEmptyCart(): Observable<Cart> {
    return this.http.post<Cart>(environment.cartServiceUrl + '/cart', null, {})
      .pipe(tap(cart => this.sessionService.setCurrentCartId(cart.id)));
  }

  deleteEntry(id?: number): Observable<any> {
    const cartId = this.sessionService.getCurrentCartId();
    return this.http.delete(environment.cartServiceUrl + '/cart/' + cartId + '/entry/' + id);
  }

  addToCart(pId: string, quantity: number): void {
    const newEntry: Entry = {
      product: {id: pId},
      quantity
    };
    this.getOrCreateCartId().subscribe(cartId => {
      this.http.post(environment.cartServiceUrl + '/cart/' + cartId + '/entry', newEntry, {})
        .subscribe(
          () => this.router.navigate(['/cart']),
          () => this.router.navigate(['/cart']));
    });
  }

  updateQuantity(id: number, quantity: number): Observable<any> {
    const entry: Entry = {id, quantity};
    const cartId = this.sessionService.getCurrentCartId();
    return this.http.patch(environment.cartServiceUrl + '/cart/' + cartId + '/entry/' + id, entry, {});
  }

  placeOrder(): void {
    const cartId = this.sessionService.getCurrentCartId();
    this.http.post(environment.cartServiceUrl + '/cart/' + cartId + '/placeOrder', null, {})
      .subscribe();
    this.sessionService.removeSessionCart();
    this.router.navigate(['']);
  }

}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../models/product';
import {environment} from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
    private http: HttpClient
  ) {
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(environment.productServiceUrl + '/product');
  }

  getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(environment.productServiceUrl + '/product/' + id);
  }

}

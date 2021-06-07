import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from '../models/product';
import {EndpointsService} from './endpoints.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(
    private http: HttpClient,
    private endpointsService: EndpointsService
  ) {}

  getProducts(): Observable<Product[]> {
    return this.endpointsService.withEndpoints(endpoints =>
      this.http.get<Product[]>(endpoints.productServiceUrl + '/product')
    );
  }

  getProduct(id: string): Observable<Product> {
    return this.endpointsService.withEndpoints(endpoints =>
      this.http.get<Product>(endpoints.productServiceUrl + '/product/' + id)
    );
  }

}

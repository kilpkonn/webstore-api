import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { News } from "../../../shared/models/news";

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  private url = 'api/news';

  constructor(private http: HttpClient) {
  }

  getNews(): Observable<News[]> {
    return this.http.get<News[]>(this.url);
  }
}

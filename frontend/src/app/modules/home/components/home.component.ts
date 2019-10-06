import { Component, OnInit } from '@angular/core';
import { NewsService } from "../services/news.service";
import { News } from "../../../shared/models/news";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  news: News[];

  constructor(private newsService: NewsService) { }

  getNews(): void {
    this.newsService.getNews()
      .subscribe(news => this.news = news);
  }

  ngOnInit() {
    this.getNews();
  }

}

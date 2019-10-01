import { Component, OnInit } from '@angular/core';

import { NewsService} from "../services/news.service";

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {

  private newsData: any;

  constructor(private svc: NewsService) { }

  ngOnInit() {
    this.svc.getNews().subscribe(data => {
      this.newsData = data;
    });
  }

}

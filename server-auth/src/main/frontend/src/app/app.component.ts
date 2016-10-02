import { Component } from '@angular/core';

import { ApiService } from './shared';

import '../style/css/gumby.css';

@Component({
  selector: 'my-app', // <my-app></my-app>
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  url = 'https://github.com/jbescos/CentralAuth';

  constructor(private api: ApiService) {
    // Do something with api
  }
}

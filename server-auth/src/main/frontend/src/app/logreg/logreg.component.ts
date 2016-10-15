import { Component, OnInit, OnDestroy } from '@angular/core';
import {Subscription } from 'rxjs';
import {Router, ActivatedRoute} from '@angular/router';
import {LogregService} from './logreg.service';
import {Dto} from './dto.logreg';

@Component({
  selector: 'my-logreg',
  templateUrl: './logreg.component.html',
  styleUrls: ['./logreg.component.scss'],
  providers: [LogregService]
})
export class LogregComponent implements OnInit, OnDestroy {

  loginUrls: Dto;
  private subscription: Subscription;
  appId: string;
  mode = 'Observable';

  constructor(private activatedRoute: ActivatedRoute, private logRegservice: LogregService) {
  }

  clicked(username:string, password1:string, password2:string) {
    console.log("Clicked register with user: "+username);
    this.logRegservice.register(username, password1, password2, this.appId).subscribe(loginUrls => this.loginUrls = loginUrls);
  }

  ngOnInit() {
    // subscribe to router event
    this.subscription = this.activatedRoute.params.subscribe(
      (param: any) => {
        this.appId = param['appId'];
        console.log("App Id: "+this.appId);
      });
  }

  ngOnDestroy() {
    // prevent memory leak by unsubscribing
    this.subscription.unsubscribe();
  }

}

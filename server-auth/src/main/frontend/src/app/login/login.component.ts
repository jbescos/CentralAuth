import { Component, OnInit, OnDestroy } from '@angular/core';
import {Subscription } from 'rxjs';
import {Router, ActivatedRoute} from '@angular/router';

@Component({
  selector: 'my-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  private subscription: Subscription;
  appId: string;

  constructor(private activatedRoute: ActivatedRoute) {
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

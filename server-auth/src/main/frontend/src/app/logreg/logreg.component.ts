import { Component, OnInit, OnDestroy } from '@angular/core';
import {Subscription } from 'rxjs';
import {Router, ActivatedRoute} from '@angular/router';
import {RestService} from '../shared/rest.service';

@Component({
  selector: 'my-logreg',
  templateUrl: './logreg.component.html',
  styleUrls: ['./logreg.component.scss'],
  providers: [RestService]
})
export class LogregComponent implements OnInit, OnDestroy {

  private subscription: Subscription;
  appId: string;
  mode = 'Observable';

  constructor(private activatedRoute: ActivatedRoute, private restService: RestService) {
  }

  clicked(username:string, password1:string, password2:string) {
    console.log("Clicked register with user: "+username);
    this.restService.register(username, password1, password2, this.appId).subscribe(loginUrls => this.restService.notifyLoginToApps(loginUrls));
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

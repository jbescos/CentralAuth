import { Component, OnInit, OnDestroy } from '@angular/core';
import {Subscription } from 'rxjs';
import {Router, ActivatedRoute} from '@angular/router';
import {RestService} from '../shared/rest.service';

@Component({
  selector: 'my-webmaster',
  templateUrl: './webmaster.component.html',
  styleUrls: ['./webmaster.component.scss'],
  providers: [RestService]
})
export class WebmasterComponent implements OnInit, OnDestroy {

  constructor(private activatedRoute: ActivatedRoute, private restService: RestService) {
  }


  ngOnInit() {
  }

  ngOnDestroy() {
  }

}

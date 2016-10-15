import {Injectable} from '@angular/core'
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable }     from 'rxjs/Observable';
import { Dto } from './dto.logreg';
 
@Injectable()
export class LogregService{
 
   endpoint_url:string = "server-auth/rest/auth/user/cookiemgr/login/register/";
   main_app_id:string = "main";
 
   constructor(private http: Http){
   }

  private extractData(res: Response):Dto{
    let body = res.json();
    return body.data || { };
  }

  private handleError (error: any) {
    let errMsg = (error.message) ? error.message : error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
 
   register(username:string, password1:string, password2:string, appId:string): Observable<Dto>{
       let params = new URLSearchParams();
       params.set('username', username);
       params.set('password1', password1);
       params.set('password2', password2);
       var app = this.main_app_id;
       if(appId == null){
           app = this.main_app_id;
       }else{
           app = appId;
       }
       return this.http.get(this.endpoint_url+app, {search: params}).map(res => this.extractData(res));
   }
}
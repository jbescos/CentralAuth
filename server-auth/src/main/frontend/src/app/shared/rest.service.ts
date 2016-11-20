import {Injectable} from '@angular/core'
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable }     from 'rxjs/Observable';
import {ApplicationDto} from './dto/application.dto';
 
@Injectable()
export class RestService{
 
   register_url:string = "server-auth/rest/account/register/";
   login_url:string = "server-auth/rest/account/login/";
   get_applications:string = "server-auth/rest/auth/admin/application/";
 
   constructor(private http: Http){
   }

  private handleError (error: any) {
    let errMsg = (error.message) ? error.message : error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
 
   register(username:string, password1:string, password2:string, appId:string): Observable<string[]>{
    let params = new URLSearchParams();
    params.set('username', username);
    params.set('password1', password1);
    params.set('password2', password2);
    if(appId != null){
      params.set('appId', appId);
    }
    return this.http.get(this.register_url, {search: params}).map(res => <string[]>res.json());
   }

   login(username:string, password:string, appId:string): Observable<string[]>{
    let params = new URLSearchParams();
    params.set('username', username);
    params.set('password', password);
    if(appId != null){
      params.set('appId', appId);
    }
    return this.http.get(this.login_url, {search: params}).map(res => <string[]>res.json());
   }

   notifyLoginToApps(urls: string[]){
    console.log("Notify apps: "+urls);
    for(let url of urls){
      this.http.get(url).subscribe(res => console.log("Logged in: "+url));
    }
   }

   getApplications(): Observable<ApplicationDto[]>{
     return this.http.get(this.get_applications).map(res => <ApplicationDto[]>res.json());
   }

}
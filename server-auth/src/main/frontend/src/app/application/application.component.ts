import {Component} from '@angular/core';
import {RestService} from '../shared/rest.service';
import {ApplicationDto} from '../shared/dto/application.dto';

@Component({
  selector: 'my-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss'],
  providers: [RestService]
})
export class ApplicationComponent {

  applications: ApplicationDto[];

  constructor(private restService: RestService) {
    restService.getApplications().subscribe(result => this.applications = result);
  }

  delete(appId:string) {
    console.log("Delete: "+appId);
  }

  update(appId:string, url:string, description:string) {
    console.log("Update: "+appId);
  }

  create(appId:string, url:string, description:string, expire:number, password:string) {
    console.log("Create: "+appId);
  }

}

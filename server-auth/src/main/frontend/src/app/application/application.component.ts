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
    this.restService.deleteApplication(appId);
    this.applications.filter(dto => dto.appId == appId).pop;
  }

  update(appId:string, url:string, description:string) {
    console.log("Update: "+appId);
    this.applications.filter(dto => dto.appId == appId).forEach(dto => {
      dto.url = url;
      dto.description = description;
      this.restService.updateApplication(dto);
    });
  }

  create(appId:string, url:string, description:string, expire:string, password:string) {
    console.log("Create: "+appId);
    var dto: ApplicationDto = new ApplicationDto();
    dto.appId=appId;
    dto.description=description;
    dto.url=url;
    dto.expireMillis = Number(expire);
    dto.password = password;
    this.restService.addApplication(dto);
    this.applications.push(dto);
  }

}

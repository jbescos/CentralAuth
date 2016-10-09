import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { LogregComponent } from './logreg/logreg.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'about', component: AboutComponent},
  { path: 'logreg', component: LogregComponent},
  { path: 'login', component: LoginComponent}
];

export const routing = RouterModule.forRoot(routes);

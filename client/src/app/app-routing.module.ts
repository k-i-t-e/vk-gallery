import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {LoginComponent} from "./gallery/login/login.component";
import {GalleryComponent} from "./gallery/gallery/gallery.component";
import {GroupsComponent} from "./gallery/groups/groups.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'gallery', component: GalleryComponent },
  { path: 'groups', component: GroupsComponent },
  { path: '', redirectTo: '/gallery', pathMatch: 'full' }
];

@NgModule({
            exports: [ RouterModule ],
            imports: [ RouterModule.forRoot(routes, {useHash: true}) ]
          })
export class AppRoutingModule {

}

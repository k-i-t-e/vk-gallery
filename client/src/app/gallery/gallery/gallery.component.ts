import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.authService.getCurrenUser().subscribe(user => {
      if (user == null) {
        this.router.navigateByUrl('/login')
      }
    })
  }

}

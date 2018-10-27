import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';
import {Router} from '@angular/router';

const TABS = {
  GALLERY: '/gallery',
  GROUPS: '/groups',
  ALBUMS: '/albums'
};

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  activeTab = 'GALLERY';
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.activeTab = Object.entries(TABS).filter(e => e[1] === this.router.url)[0][0]
  }

  logout() {
    this.authService.logout().subscribe(ignored => this.router.navigateByUrl('/login'))
  }

  public tabSelect(tab: string) {
    this.router.navigateByUrl(TABS[tab]).then(() => this.activeTab = tab)
  }

  public isActive(tab: string) {
    return this.activeTab === tab
  }
}

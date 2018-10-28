import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';
import {NavigationEnd, Router} from '@angular/router';

const TABS = {
  GALLERY: '/gallery',
  GROUPS: '/groups',
  ALBUMS: '/albums'
};

const rootUrlRegex = /(\/\w+)\/?/;

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  activeTab = 'GALLERY';
  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const matchUrl = rootUrlRegex.exec(event.url);
        this.activeTab = Object.entries(TABS).filter(e => e[1] === matchUrl[1])[0][0]
      }
    });

    const match = rootUrlRegex.exec(this.router.url);
    this.activeTab = Object.entries(TABS).filter(e => e[1] === match[1])[0][0]
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

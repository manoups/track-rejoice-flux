import {Component, inject, OnInit} from '@angular/core';
import {WebsocketService} from '../../common/websocket.service';
import {publishEvent, subscribeTo} from '../../common/app-common-utils';
import {take} from 'rxjs';
import {AppContext} from '../../app-context';

@Component({
  selector: 'track-rejoice-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  context = AppContext;
  socketService: WebsocketService<any> = inject(WebsocketService<any>);
  authService: AuthenticationService = inject(AuthenticationService);

    ngOnInit(): void {
      const subscription = subscribeTo("/api/user");
      subscription.pipe(take(1)).subscribe({
        next: userProfile => {
          if (userProfile) {
            this.socketService.initialise("api/updates", update => publishEvent(update.type, update));
          } else {
            this.authService.signout();
          }
        },
        error: () => this.authService.signout()
      });
      subscription.subscribe(userProfile => AppContext.setUserProfile(userProfile));
    }

}

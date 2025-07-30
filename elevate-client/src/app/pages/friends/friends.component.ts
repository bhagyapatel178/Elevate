import {Component,ChangeDetectionStrategy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {Subscription} from 'rxjs';

interface UserSearchResult {
  id: number;
  username: string;
}

@Component({
  selector: 'app-friends',
  imports: [CommonModule,FormsModule],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.scss',
})
export class FriendsComponent{

  friendToSearch = ''

  result?: UserSearchResult;
  error = '';

  constructor(private http: HttpClient) {}

  findUser(): void {
    this.error = '';
    const q = this.friendToSearch.trim();
    if (!q) { this.result = undefined; return; }

    const params = new HttpParams().set('username', q);

    this.http.get<UserSearchResult>('/api/users/search', { params })
      .subscribe({
        next: res => this.result = res,
        error: err => {
          this.result = undefined;
          this.error = err.status === 404 ? 'User not found' : 'Server error';
        }
      });
  }

  sendRequest(receiverId: number): Subscription {
    return this.http.post<void>('/api/friend-request/send', receiverId).subscribe({
      next: ()  => console.log('Request sent'),
      error: () => console.error('Error sending request')
    });
  }


}

import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import {FormsModule} from '@angular/forms';

interface UserSearchResult {
  id: number;
  username: string;
}

interface IncomingRequest {
  requestId: number;
  senderId:  number;
  senderUsername: string;
}

interface FriendResponse {
  id: number;
  username: string;
}

@Component({
  selector: 'app-friends',
  imports: [CommonModule,FormsModule],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.scss',
})
export class FriendsComponent implements OnInit{

  friendToSearch = ''
  error = '';
  searchResult?: UserSearchResult
  pendingAdd = false;

  incoming: IncomingRequest[] = [];
  friends : FriendResponse[]  = [];
  busyIds = new Set<number>();          // disables buttons per row

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadIncoming();
    this.loadFriends();
  }


  findUser(): void {
    this.error = '';
    this.searchResult = undefined;

    const q = this.friendToSearch.trim();
    if (!q) { this.searchResult = undefined; return; }

    const params = new HttpParams().set('username', q);

    this.http.get<UserSearchResult>('/api/users/search', { params })
      .subscribe({
        next: res => this.searchResult = res,
        error: err => {
          this.pendingAdd    = false;
          this.error = err.status === 404 ? 'User not found' : 'Server error';
        }
      });
  }

  sendRequest(id: number): void {
    if (this.pendingAdd) { return; }
    this.pendingAdd = true;

    this.http.post<void>('/api/friend-requests', { receiverId: id })
      .subscribe({
        next: ()  => {/* keep “Pending…” label */},
        error: () => { this.pendingAdd = false; }
      });
  }

  loadIncoming(): void {
    this.http.get<IncomingRequest[]>('/api/friend-requests/incoming')
      .subscribe(list => this.incoming = list);
  }

  actOnRequest(r: IncomingRequest, action: 'accept' | 'decline'): void {
    if (this.busyIds.has(r.requestId)) { return; }
    this.busyIds.add(r.requestId);

    const call = action === 'accept'
      ? this.http.put<void>(`/api/friend-requests/${r.requestId}/accept`,  null)
      : this.http.put<void>(`/api/friend-requests/${r.requestId}/decline`, null);

    call.subscribe({
      next: () => {
        this.incoming = this.incoming.filter(x => x !== r);
        this.busyIds.delete(r.requestId);
        if (action === 'accept') { this.loadFriends(); }
      },
      error: () => this.busyIds.delete(r.requestId)
    });
  }

  //friends
  loadFriends(): void {
    this.http.get<FriendResponse[]>('/api/friends')
      .subscribe(list => this.friends = list);
  }

  removeFriend(id: number): void {
    if (this.busyIds.has(id)) { return; }
    this.busyIds.add(id);

    this.http.delete<void>(`/api/friends/${id}`)
      .subscribe({
        next: () => {
          this.friends = this.friends.filter(f => f.id !== id);
          this.busyIds.delete(id);
        },
        error: () => this.busyIds.delete(id)
      });
  }

}

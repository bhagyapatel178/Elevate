import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {CommonModule, NgIf} from '@angular/common';


interface ProgressLog {
  id: number;
  liftType: string;
  variation: string;
  weight: number;
  reps: number;
  date: string;
  userId: number;
}

@Component({
  selector: 'app-profile',
  imports: [FormsModule,CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements  OnInit{

  userProfile= {
    username: '',
    email: '',
    gender: '',
    age: 0,
    preferredUnitSystem: '',
    height: 0,
    weight: 0
  };

  newLog = {
    liftType: '',
    variation: '',
    weight:null,
    reps: null
  };

  liftOptions = [
    { label: 'Bench Press', value: 'BENCH_PRESS' },
    { label: 'Squat', value: 'SQUAT' },
    { label: 'Deadlift', value: 'DEADLIFT' },
    { label: 'Dumbbell Press', value: 'DUMBBELL_PRESS' },
    { label: 'Pull Ups', value: 'PULL_UPS' },
    { label: 'Dips', value: 'DIPS' }
  ];

  progressLogs: ProgressLog[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any>('/api/users/me').subscribe({
      next: (res: any) => this.userProfile = res,
      error: err => alert('Error registering user')
    })
    this.fetchLogs();
  }

  createLog(){
    this.http.post('api/progress-logs', this.newLog).subscribe({
      next: () => this.fetchLogs(),
      error: err => alert('Error creating log')
    })
  }

  fetchLogs(){
    this.http.get<any>('/api/users/me/logs').subscribe({
      next: (res: any) => this.progressLogs = res,
      error: err => alert('Error fetching logs')
    })
  }

}


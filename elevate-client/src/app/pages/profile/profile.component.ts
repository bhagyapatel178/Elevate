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

  isEditing = false;

  updatedProfile = {
    username: '',
    email: '',
    gender: '',
    age: 0,
    preferredUnitSystem: '',
    height: 0,
    weight: 0
  };

  userProfile= {
    username: '',
    email: '',
    gender: '',
    age: 0,
    preferredUnitSystem: '',
    height: 0,
    weight: 0
  };

  newLift = {
    liftType: '',
    variation: ''
  };

  sets: { weight: number | null, reps: number | null }[] = [
    { weight: null, reps: null }
  ];

  liftOptions = [
    { label: 'Bench Press', value: 'BENCH_PRESS' },
    { label: 'Squat', value: 'SQUAT' },
    { label: 'Deadlift', value: 'DEADLIFT' },
    { label: 'Dumbbell Press', value: 'DUMBBELL_PRESS' },
    { label: 'Pull Ups', value: 'PULL_UPS' },
    { label: 'Dips', value: 'DIPS' }
  ];

  genderOptions = [
    { label: 'Male', value: 'MALE' },
    { label: 'Female', value: 'FEMALE' },
    { label: 'Non-binary', value: 'NON_BINARY' },
    { label: 'Prefer not to say', value: 'PREFER_NOT_TO_SAY' }
  ];

  unitOptions = [
    { label: 'Metric (kg, cm)', value: 'METRIC' },
    { label: 'Imperial (lbs, in)', value: 'IMPERIAL' }
  ];

  progressLogs: ProgressLog[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any>('/api/users/me').subscribe({
      next: (res: any) => {
        this.userProfile = res;
        this.updatedProfile = {...res}; // clones data
      },
      error: err => alert('Error registering user')
    })
    this.fetchLogs();
  }

  getLiftOutput(liftType: string){
    if (liftType == 'BENCH_PRESS'){return 'Bench Press'}
    if (liftType == 'SQUAT'){return 'Squat'}
    if (liftType == 'DEADLIFT'){return 'Deadlift'}
    if (liftType == 'DUMBBELL_PRESS'){return 'Dumbbell Press'}
    if (liftType == 'PULL_UPS'){return 'Pull Ups'}
    if (liftType == 'DIPS'){return 'Dips'}
    return ''
  }

  createLog(){
    const logsToSend = this.sets.map(set => ({
      liftType: this.newLift.liftType,
      variation: this.newLift.variation,
      weight: set.weight,
      reps: set.reps
    }));

    logsToSend.forEach(log => {
      this.http.post('api/progress-logs', log).subscribe({
        next: () => this.fetchLogs(),
        error: err => alert(err.error?.message)
      });
    });
    this.newLift = { liftType: '', variation: '' };
    this.sets = [{ weight: null, reps: null }];
  }



  fetchLogs(){
    this.http.get<any>('/api/users/me/logs').subscribe({
      next: (res: any) => this.progressLogs = res,
      error: err => alert('Error fetching logs'),

    })
  }

  updateUser() {
    this.http.put(`/api/users/edit`, this.updatedProfile).subscribe({
      next: () => {
        this.userProfile = { ...this.updatedProfile };
        this.isEditing = false;
      },
      error: err => {
        console.error(err);
        alert(err.error?.message || 'Update failed')
      }
    });
  }

  getGenderLabel(value: string): string {
    const match = this.genderOptions.find(option => option.value === value);
    return match ? match.label : value;
  }

  getUnitLabel(value: string): string {
    const match = this.unitOptions.find(option => option.value === value);
    return match ? match.label : value;
  }


}


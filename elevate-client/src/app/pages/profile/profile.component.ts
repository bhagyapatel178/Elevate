import {Component, HostListener, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';


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

  // progressLogs: ProgressLog[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any>('/api/users/me').subscribe({
      next: (res: any) => {
        this.userProfile = res;
        this.updatedProfile = {...res}; // clones data
      },
      error: err => alert('Error registering user')
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

//   get biometricsIncomplete(): boolean {
//     const { gender, weight, height } = this.userProfile;
//     return !(gender && weight && height);
//   }
//
//   showInfo = false;   // toggled by the icon
//
// // clickOutside directive: quick inline helper
//   @HostListener('document:click', ['$event'])
//   closeOnOutside(event: Event) {
//     const path = event.composedPath?.() || [];
//     const clickedInsidePopover = (path as HTMLElement[]).some(el =>
//       (el as HTMLElement).classList?.contains?.('info-pop') ||
//       (el as HTMLElement).classList?.contains?.('info-icon')
//     );
//     if (!clickedInsidePopover) this.showInfo = false;
//   }

}

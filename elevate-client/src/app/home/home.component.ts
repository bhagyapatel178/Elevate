import { Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

import { CalendarModule, CalendarEvent, CalendarView} from 'angular-calendar';
import { isSameDay, isSameMonth} from 'date-fns';
import { EventColor } from 'calendar-utils';

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
  selector: 'app-home',
  imports: [FormsModule,CommonModule,CalendarModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  view: CalendarView = CalendarView.Month;
  viewDate = new Date();
  events: CalendarEvent[] = [];

  activeDayIsOpen = false;
  showAddForm = false;

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

  private readonly LIFT_COLOURS: Record<string, EventColor> = {
    BENCH_PRESS:  { primary: '#fd1717', secondary: '#FAE3E3' },
    SQUAT:        { primary: '#1e90ff', secondary: '#D1E8FF' },
    DEADLIFT:     { primary: '#e3d808', secondary: '#FDF1BA' },
    DUMBBELL_PRESS:{ primary: '#00a945', secondary: '#D1E8FF' },
    PULL_UPS:     { primary: '#e91eff', secondary: '#D1E8FF' },
    DIPS:         { primary: '#08e3c9', secondary: '#FDF1BA' }
  };

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchLogs();
  }

  fetchLogs(){
    this.http.get<ProgressLog[]>('/api/users/me/logs').subscribe(logs => {
      this.events = logs.map(l => ({
        start: new Date(l.date),
        title: `${this.getLiftOutput(l.liftType)} ${l.variation}: ${l.weight}kg × ${l.reps}`,
        color: this.selectLiftColour(l.liftType),
        allDay: true
      }))
    });
  }

  createLog(){
    const logsToSend = this.sets.map(set => ({
      liftType: this.newLift.liftType,
      variation: this.newLift.variation,
      weight: set.weight,
      reps: set.reps
    }));

    logsToSend.forEach(log => {
      this.http.post('/api/progress-logs', log).subscribe({
        next: () => this.fetchLogs(),
        error: err => alert(err.error?.message)
      });
    });
    this.newLift = { liftType: '', variation: '' };
    this.sets = [{ weight: null, reps: null }];
  }

  getLiftOutput(liftType: string) {
    if (liftType == 'BENCH_PRESS') {return 'Bench Press'}
    if (liftType == 'SQUAT') {return 'Squat'}
    if (liftType == 'DEADLIFT') {return 'Deadlift'}
    if (liftType == 'DUMBBELL_PRESS') {return 'Dumbbell Press'}
    if (liftType == 'PULL_UPS') {return 'Pull Ups'}
    if (liftType == 'DIPS') {return 'Dips'}
    return ''
  }

  selectLiftColour(liftType: string): EventColor {
    return this.LIFT_COLOURS[liftType] ?? { primary: '#ffffff', secondary: '#ffffff' };
  }

  dayClicked({date, events,}: { date: Date; events: CalendarEvent[]; }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.viewDate = date;
      this.activeDayIsOpen =
        isSameDay(this.viewDate, date) && events.length !== 0 && !this.activeDayIsOpen;
    }
  }

}

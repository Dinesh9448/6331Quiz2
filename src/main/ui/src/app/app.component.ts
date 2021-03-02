import {HttpClient} from '@angular/common/http';
import {Component, ViewChild, AfterViewInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Page } from 'ngx-pagination/dist/pagination-controls.directive';
import {merge, Observable, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {
  displayedColumns: string[] = ['year', 'state', 'statePo', 'candidate', 'partyDetailed', 'candidateVotes', 'totalVotes', 'partySimplified'];
  exampleDatabase: ExampleHttpDatabase | null;
  data: PresidentElect[] = [];
  queryStatistics: Statistics[] = [];
  filterId: number = 0;

  private url: string = 'president-elect?';

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _httpClient: HttpClient) {}

  element: string = '_embedded.earthquake';

  ngAfterViewInit() {
    this.exampleDatabase = new ExampleHttpDatabase(this._httpClient, this.url);

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.exampleDatabase!.getEarthQuakeContent(
            this.sort.active, this.sort.direction, this.paginator.pageIndex);
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          this.resultsLength = data.presidentElects.totalElements;
          this.queryStatistics = data.queryStatistics;

          return data.presidentElects.content;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          // Catch if the  API has reached its rate limit. Return empty data.
          this.isRateLimitReached = true;
          return observableOf([]);
        })
      ).subscribe(data => this.data = data);
  }

  onSelected(filterId: number): void {
    console.log(`not yet implemented` + filterId);
    this.filterId = filterId;
    if(0==filterId){
      this.url = 'president-elect?';
      this.queryStatistics = [];
      this.ngAfterViewInit();
    }
  }
  byYearAndState(year: string, statePO: string, times: number): void {
    this.url = `/president-elect/findByYearEqualsAndStatePoEquals?year=${year}&statePo=${statePO}&times=${times}&`;
    this.exampleDatabase = null;
    this.data = [];
    this.queryStatistics = [];
    this.ngAfterViewInit();
  }
  byCandidateName(name: string, times: number): void {
    this.url = `/president-elect/findByCandidateLike?name=${name}&times=${times}&`;
    this.exampleDatabase = null;
    this.data = [];
    this.queryStatistics = [];
    this.ngAfterViewInit();
  }

  byCandidateVotesAndYears(startVote: string, endVote: string, startYear: string, endYear: string, times: number): void {
    this.url = `/president-elect/findByCandidateVotesBetweenAndYearBetween?startVotes=${startVote}&endVotes=${endVote}&startYear=${startYear}&endYears=${endYear}&times=${times}&`;
    this.exampleDatabase = null;
    this.data = [];
    this.queryStatistics = [];
    this.ngAfterViewInit();
  }

  myForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    file: new FormControl('', [Validators.required]),
    fileSource: new FormControl('', [Validators.required])
  });

  get f(){
    return this.myForm.controls;
  }

  onFileChange(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.myForm.patchValue({
        fileSource: file
      });
    }
  }

  submit(){
    const formData = new FormData();
    formData.append('file', this.myForm.get('fileSource').value);

    this._httpClient.post('earthquake/uploadFile', formData)
      .subscribe(res => {
        console.log(res);
        alert('Uploaded Successfully.');
      })
  }

}

export interface EarthQuakeAPI{
  _embedded: EarthQuakeArray;
  page: PageDtl;
}
export interface EarthQuakeArray{
  presidentElects: PresidentElect[];
  queryStatistics: Statistics[];
}

export interface Statistics{
  query: String;
  executionTime: number;
}

export interface PageDtl{
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}

export interface PresidentElect{
  year: number;
  state: string;
  statePo: string;
  candidate: string;
  partyDetailed: string;
  candidateVotes: number;
  totalVotes: number;
  partySimplified: string;
}

export interface EarthQuakeContent {
  presidentElects: EarthQuakeBody;
  queryStatistics: Statistics[];
}

export interface EarthQuakeBody extends PageDtl{
  content: PresidentElect[];
}

export class ExampleHttpDatabase {
  constructor(private _httpClient: HttpClient, private href: string) {}

  getEarthQuakeDetails(sort: string, order: string, page: number): Observable<EarthQuakeAPI> {
    //const href = 'http://localhost:808/earthquake';
    const requestUrl =
        `${this.href}sort=${sort},${order}&page=${page}`;

    return this._httpClient.get<EarthQuakeAPI>(requestUrl);
  }

  getEarthQuakeContent(sort: string, order: string, page: number): Observable<EarthQuakeContent> {
    const requestUrl =
        `${this.href}sort=${sort},${order}&page=${page}`;

    return this._httpClient.get<EarthQuakeContent>(requestUrl);
  }
}

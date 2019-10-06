import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FlexLayoutModule } from '@angular/flex-layout';
import { AgmCoreModule, GoogleMapsAPIWrapper } from '@agm/core';

import { ContactRoutingModule } from './contact-routing.module';
import { ContactComponent } from './components/contact.component';
import {MapComponent} from "./components/map/map.component";


@NgModule({
  declarations: [
    ContactComponent,
    MapComponent
  ],
  imports: [
    CommonModule,
    ContactRoutingModule,
    FlexLayoutModule,
    AgmCoreModule.forRoot({apiKey: 'AIzaSyADf2Ss5DEZdZMFEJ0f8fmi1KcYRZMYLZI'}),
  ],
  providers: [
    GoogleMapsAPIWrapper
  ]
})
export class ContactModule { }

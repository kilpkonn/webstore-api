import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FlexLayoutModule } from '@angular/flex-layout';

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
  ],
  providers: [
  ]
})
export class ContactModule { }

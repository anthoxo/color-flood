import { Component, computed, input } from '@angular/core';
import { NgClass } from '@angular/common';

const COLORS = [
  'green',
  'gray',
  'indigo',
  'fairy-tale',
  'red-wood',
  'yellow',
  'air-force-blue',
  'dark-brown',
  'columbia-blue',
  'night',
  'cerise',
  'blue',
  'emerald',
  'white',
  'rose',
  'turquoise',
  'dark-purple',
  'red',
  'slate-blue',
  'orange',
];

@Component({
  selector: 'square',
  templateUrl: 'square.component.html',
  styleUrl: 'square.component.scss',
  standalone: true,
  imports: [
    NgClass
  ]
})
export class SquareComponent {
  color = input.required<number>();
  colorClass = computed(() => COLORS[this.color()]);
}
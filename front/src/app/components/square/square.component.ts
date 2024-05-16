import { Component, computed, input } from '@angular/core';
import { NgClass } from '@angular/common';
import { Joker } from '../../models/grid.model';

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
  joker = input.required<Joker>();
  colorClass = computed(() => COLORS[this.color()]);
  jokerIcon = computed(() => {
    if (this.joker() === 'ZAP') {
      return '⚡️'
    }
    if (this.joker() === 'SHADOW') {
      return '🌩️'
    }
    if (this.joker() === 'SHIELD') {
      return '🛡️'
    }
    if (this.joker() === 'ARCANE_THIEF') {
      return '😈'
    }
    return '';
  });

}
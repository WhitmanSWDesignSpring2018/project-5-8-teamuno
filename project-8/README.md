
For this project, we implemented a MIDI file importer and a menu item for
changing the instrument of selected notes.

We started with a MIDI reader prototype, then integrated it into the project as
a MidiAdapter class. This obviously uses the adapter pattern. This is elegant
because it avoids coupling between the project and the MidiAdapter; The
MidiAdapter knows about the NoteBar class, but not vice versa. For the
instrument changer, we added a menu item that changes the selected notes to the
currently selected instrument. This doesn't require any new classes.

We've managed to introduce a lot of new faults in this project, and not just in
the MIDI importing feature. Despite that, we haven't introduced much new
inelegance. The MidiAdapter class is divided into a number of private static
methods; These pass arguments among one another frequently, which may be
inelegant.

We spent about 6.5 hours of pair programming time on this project, and we
estimated that we would take 9 hours of pair programming to finish this. This
gives us a misleading velocity of 1.4. This is lower than our previous
velocity of 2.9, but both velocities indicate how our work was rushed by our
own circumstances.

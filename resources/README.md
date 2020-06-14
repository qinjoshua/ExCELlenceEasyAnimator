# ExCELlence Easy Animator
## Nicholas Miklaucic, Joshua Qin

This file explains the high-level design of our animator application. Due to the fact that we forgot to submit a
`README` file for the last assignment, the design section of this document explains the design as it currently is, and
the changes section explains how this design evolved from the one submitted on the last assignment.

## Design
We use a classic MVC architecture. The model is responsible for computing the state of each shape at any given
tick, adding keyframes to change that state appropriately, and storing the canvas origin and dimensions. The view is
responsible for, given a read-only window into the model state, rendering that output. As of now, this is the textual
format from last time, SVG output, and a Swing rendering of the animation. The controller is responsible for reading in
user input (specifically, the CLI arguments that are used to determine input and output and to configure the view type
and speed) and giving the view and model the requisite configuration needed to produce the desired output.
### Model
#### `AnimatorModel`
`AnimatorModel`, an interface, represents a single animation: the data necessary to compute the state of each shape at
any particular tick. We use a keyframe-based approach as opposed to a motion-based approach. The reason for this is
that the best way to deal with invalid input is to make invalid input impossible. Many of the ways in which motion-based
animation initialization can be invalid are simply impossible in a keyframe-based approach. This reduces the possibility
of errors considerably without making the code any more error-prone at any other step. It's very easy to go from motions
to keyframes: a motion is just two keyframes. On the other hand, going from keyframes to motions isn't easy, and so we
think our choice to use keyframes made writing the code for this part significantly easier.

The model interface also includes getters and setters for the canvas origin X and Y coordinate, the canvas width, and
the canvas height. The speed is considered a view-specific detail.
#### `ReadOnlyAnimatorModel`
The `AnimatorModel` interface actually extends a viewmodel, `ReadOnlyAnimatorModel`, which isolates those methods that
don't modify the model itself: this is the `shapesAt` method and the getters for the canvas origin, width, and height.
#### `AnimatorModelImpl`
`AnimatorModelImpl` is the only class, as of this submission, that implements `AnimatorModel`. It uses
`LinkedHashMap<String, SortedSet<Frame>>` as a data structure: a map from shape names to a sorted set of `Frame`s, an
interface that will be covered shortly that essentially describes a single shape at a single time. (The frames are
sorted by time, which makes searching for the correct two frames to interpolate between at a single time easier.) Where
m is the number of shapes and n is the number of keyframes per shape, this data structure is O(m + n) space complexity.
Adding new keyframes is O(1), and getting the keyframes around a specific time to interpolate is O(n) as it is currently
written but could be optimized to O(log n) using binary search if needed.
#### `Shape`, `AShape`, `Rectangle`, and `Ellipse`
`Shape` represents all of the data for a specific shape: as of now this is a rectangle or an ellipse. It has a width,
height, position (for rectangles, this is the upper left: for ellipses, this is the center), and color, represented in
sRGB.

It has an `AShape` abstract base class that implements common functionality and two subclasses, `Rectangle` and
`Ellipse`, that implement their own specific functionality. There's an enumerated type, `ShapeType`, that is used as a
discriminant to distinguish different kinds of shapes.
#### `Frame` and `FrameImpl`
We needed a separate data structure for handling shapes combined with times, and `Frame` is what resulted. `Frame`s are
composed of a shape and a time. Because they're the lowest-level class with this information, they're where the actual
math of interpolation is handled. `FrameImpl` is the only implementaiton of `Frame` as of this submission, and is a
fairly straightforward implementation with a `Shape` and `double` field for the shape and time respectively.
#### `Posn` and `PosnImpl`
To store positions, we use a `Posn` class that stores positions using doubles. This brings us to another important
design decision: our model works entirely in `double`s, including this `Posn` class. That's because, in our reckoning,
the model coordinate system is idealized and can theoretically be any sort of precision. Actually translating that
understanding to the world of pixels on a screen is a view job, and we have our views handle the rounding and truncation
necessary to do that. `Color`s are the exception, because the `rgb` constructor expects floats if using the
floating-point representation instead of the 0-255 hex representation, but we still eschew integral types. Using
floating-points also means that our results are more exact than they would be for integers, and that precision more or
less has no impact from scaling.
### View
We chose to segregate our view interfaces, because we saw no point in promising methods our classes couldn't deliver:
what was the Swing view supposed to do when given an `Appendable`? We found throwing `UnsupportedOperationException`
wasn't a very good solution in light of the fact that it removes one of the main benefits of Java's type system and
almost seemed to defeat the purpose of an interface. In view of that, there are three separate interfaces and their
implementations:
#### `TextView` and `TextViewImpl`
This is the textual view as done in Assignment 5 and refined in this submission. There isn't really much to comment on
here, as it was basically completely specified.
#### `SVGView` and `SVGViewImpl`
TODO Joshua you got this
#### `VisualView` and `SwingView`
This is the Swing visual view. `VisualView` is the interface (with a single `void` method that runs the program), and
`SwingView` is the current only implementation of this interface. To translate between the model's idea of shapes, as
encoded by `ShapeType`, and Swing's idea of shapes, as encoded by `java.awt.Shape`, a command pattern is used, where
each `ShapeType` maps to a command that can constitute a collection of shapes or a single shape. (Here, because Swing
has rectangles and ellipses, there's no need for this, but the command design pattern gives us the flexibility to do
this if future shape types required it.)
### Controller
TODO when we figure this out

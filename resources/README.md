# ExCELlence Easy Animator
## Nicholas Miklaucic, Joshua Qin

This file explains the high-level design of our animator application. The sections pertaining to our model and the
previous view designs are mostly unchanged: the changes section lists the few exceptions. The below additions section
indicates what has been added.

## Additions
 - The "Model" section has small modifications, but no additions at the type level.
 - The "View" section has three new views, "Editor", "Menu", "Player", that together comprise the new Assignment 7
   functionality. (The menu view is a little special: it's a splash screen of sorts that can be accessed by starting the
   program without any arguments. It wasn't explicitly required, but we feel it makes the editor significantly more
   user-friendly.)
 - The "Controller" section has significantly increased functionality and is appreciably more heavyweight, so
   its sections is almost entirely new.
## Changes
 - The model now has functionality for removing keyframes and shapes to support the corresponding editor functionality.
 - The model now finds the keyframes around a specific tick (for interpolation) using the standard Java implementation
   of `NavigableSet`, which is more performant than the linear search that was done previously.
 - Shapes now have setters, which are used to modify existing shapes as per the editor view requirements.
 - There's a new section under "Design", "Architecture", explaining how the design choices around the editor view were
   made and what those choices are.

## Design
We use a classic MVC architecture. The model is responsible for computing the state of each shape at any given tick,
adding keyframes to change that state appropriately, and storing the canvas origin and dimensions. The view is
responsible for, given a read-only window into the model state, rendering that output. The controller is responsible for
reading in user input (specifically, the CLI arguments that are used to determine input and output and to configure the
view type and speed) and giving the view and model the requisite configuration needed to produce the desired output.

### Architecture
Having an editor view that can make permanent modifications to the model means that the architecture used for the
previous views, where the view received a read-only view model and was therefore prevented from ever making changes,
faced some challenges. We strove to find a design that would maintain a virtually complete separation of our model,
view, and controller, such that the view was still prevented from directly effecting changes to the model. Another
issue is communication between parts of the editor view: our properties panel, for instance, makes changes that must be
reflected immediately in the other panels. This is ideally achieved without direct references between different panels
of the editor view or any other dependency inversion.

Our solution to this is to wrap changes to the model, editor view, or player view in a command design pattern. The
editor view and any subpanels that need one or both of them receive *callbacks* that accept `AnimatorAction`s for
editing the model and `EditorAction`s for editing the view. These commands are restricted to a specific set of
operations, preventing unrestricted mutation of the model or view, and the controller (broadly referring to all of the
classes in the `controller` package) has total control over what actions get propagated to the model and whether
anything concomitant modifications happen. This prevents dependency inversion, because each view is simply promised that
callbacks will work in some way and are then designed around the read-only model state. We believe this approach
provides maximum modularity and reusability.

### Model
#### `AnimatorModel`
`AnimatorModel`, an interface, represents a single animation: the data necessary to compute the state of each shape at
any particular tick. We use a keyframe-based approach as opposed to a motion-based approach. The reason for this is
that the best way to deal with invalid input is to make invalid input impossible. Many of the ways in which motion-based
animation initialization can be invalid are simply impossible in a keyframe-based approach. This reduces the possibility
of errors considerably without making the code any more error-prone at any other step. It's very easy to go from motions
to keyframes: a motion is just two keyframes. On the other hand, going from keyframes to motions isn't easy, and so we
think our choice to use keyframes made writing the code for this part significantly easier.

Removing keyframes, which was not required for the previous parts, does complicate this structure, but importantly it
doesn't significantly weaken the "illegal states are unrepresentable" property that makes this model comparatively easy
to work with. (The only real issue is that it's now possible for a shape to have no keyframes associated with it: this
can be fixed by simply checking if the keyframe being removed is the last one for a shape and then removing that shape
entirely if that's the case.)

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
The `SVGView` interface represents a view that outputs SVG-formatted text from the given model
. The SVG View will produce the resulting SVG with no indentations of the tags. In order to
interpret the model shapes into shapes that are understood by the SVG, we used a command-design
strategy by mapping model shape types to utility functions of type `SVGShape` in the svgshapes 
package, which corresponded with model shapes. Each `SVGShape` is able to interpret a model shape 
and create the relevant SVG view from it, and the use of the command design pattern allows future 
shape support to be easily added with minimal changes to the SVGView class.

'SVGView' also takes advantage of `SVGTag` and `SVGTagAttribute`, which are data representations 
of individual components within an SVG. Using these data representations allow us to easily build 
an SVG document.
#### `VisualView` and `SwingView`
This is the Swing visual view. `VisualView` is the interface (with a single `void` method that runs the program), and
`SwingView` is the current only implementation of this interface. To translate between the model's idea of shapes, as
encoded by `ShapeType`, and Swing's idea of shapes, as encoded by `java.awt.Shape`, a command pattern is used, where
each `ShapeType` maps to a command that can constitute a collection of shapes or a single shape. (Here, because Swing
has rectangles and ellipses, there's no need for this, but the command design pattern gives us the flexibility to do
this if future shape types required it.)

#### `EditorView` and `EditorViewImpl`
This is the editor view as required for assignment 7. It has a lot of subpanels and different classes that implement
many of the required interfaces and extend Swing components. As such, instead of going through class-by-class (which
would mostly be mouse adapters or click listeners), I'll describe the layout more from the perspective of how it looks.

The central panel is `CanvasPanel`, which renders the current state of the animation at the current tick of the editor
to the screen. You can click on shapes, resize them and move them around, and drag new shapes into existence using the
toolbar. This functionality is implemented in the `view.editor.boundingbox` package, and includes the `Anchor` and
`BoundingBox` types. Speaking of the toolbar, that is on the left: lets you play the animation, export it to a text or
SVG file, and create new shapes. It is implemented via `ToolbarPanel`.

To the north is the `BannerPanel`, which adds some important information (the current name and shape), and additionally
has a button to delete the current frame. To the east is the `PropertyPanel`: when there is no shape highlighted this is
blank, but when a shape is highlighted it adds a `PropertiesPanel` for that shape that lets you edit the x and y
coordinates, width, height, and color. The color is changed using a custom color picker, `LCHColorChooser`, that
provides numerous interface improvements to the standard Swing picker and a custom preview panel that's also interactive
(clicking on a color will select it).

To the south is a `TimelinesPanel`, which shows the timeline of frames for each shape and tick. Ticks that have a dot
are currently keyframes: clicking any button moves the view to that tick and creates a new keyframe for the shape. The
panel on the right has a button to add a keyframe at any time (which allows the user to add a keyframe beyond the last
current frame) and a button to delete the shape entirely, for each shape in the animation. Each of these timelines is a
`TimelinePanel`.

#### `PlayerView` and `PlayerViewImpl`
The `PlayerView` is the view we implemented for showing an animation through to completion. It has an internal
`SwingView` that is used to render the animation, and additionally it has some extra controls for modifying how the
animation plays. 

#### `MenuView` and `MenuViewImpl`
This view provides a simple splash screen to introduce the editor view if no arguments are given to the program. It
allows the user to either create a new model from scratch or import a model from a text file.

### Controller
The controller is the class that is run in the main method, and acts as an entry point for the 
application. It processes the arguments that the user passes in to the program, and runs the 
program with those settings. The flags that the controller looks for include in, whose value 
should be the file from which the animator should read from, out, which indicates the file that 
the program should write the output to, speed, which tells the program what the speed of the
animation should be in frames per second, and view, which tells the program which view to run the
application in. Arguments that do not meet the specified formats will result in the program 
terminating with a one-sentence error message being printed out into the console.
#### `AnimatorAction`, `EditorAction`, `MenuAction`,  & `PlayerAction`
As discussed in the "Architecture" section above, we use a command design pattern to represent the different things
views can ask the controller to do. These are all given to separate callbacks that implement `Consumer` for their
respective action types. These callbacks abstract out the exact functionality required to implement different actions,
and the classes that implement them further abstract their implementation by taking in an interface instead of a
concrete class: `AnimatorModel` for `AnimatorAction`, `MenuView` for `MenuAction`, `EditorView` for `EditorAction`, and
`PlayerView` for `PlayerAction`. This ensures Liskov substitution preserves the program structure. The following lists
all of the actions with a brief description:

##### `AnimatorAction`
 - `ChangeColor`, `ChangeWidth`, `ChangeHeight`, `ChangeX`, and `ChangeY` all modify the respective values for a
   specific shape and tick.
 - `CreateKeyframe` creates a new keyframe at the specified shape and tick.
 - `CreateNewShape` creates an entirely new shape with a single new keyframe.
 - `DeleteShape` deletes a shape's entire timeline and all of its associated keyframes.
 - `RemoveKeyframe` deletes a keyframe, removing the shape as well if necessary.
 
##### `EditorAction`
 - `CreateShape` initializes the process of creating a shape, and lets the user interactively set the new shape's
   location on the screen.
 - `HighlightShape` lets the user edit a particular shape, showing an interactive bounding box that lets the user resize
   and move the shape and a properties panel that lets the user edit the different attributes of the shape
   interactively.
 - `OpenPreview` opens the preview panel and plays the animation as it currently exists.
 - `RefreshView` updates the view state to reflect the model state.
 - `SetTick` updates the view state to reflect the model state at a specific time.
 
##### `MenuAction`
 - `NewProject` creates a new, empty animation and initializes the editor view.
 - `OpenFile` initializes the model from an existing file and then initializes the editor view.
 
##### `PlayerAction`
 - `DecreaseSpeed` slows the player speed down.
 - `IncreaseSpeed` makes the player speed up.
 - `Restart` makes the player reset the time to 0 and restart the animation.
 - `ToggleLoop` toggles whether the player loops when the animation finishes.
 - `TogglePlay` toggles whether the player is currently playing or paused.





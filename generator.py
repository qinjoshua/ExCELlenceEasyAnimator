#!/usr/bin/env python3
import math
import random

def gen_single_rotation(filename):
    CANVAS_ORIGIN = (-200, -200)
    CANVAS_DIMS = (400, 400)
    with open(filename, 'w') as out:
        out.write("canvas {} {} {} {}\n".format(*CANVAS_ORIGIN, *CANVAS_DIMS))
        out.write("shape R rectangle\n")
        angle = 0
        x, y = 0, 0
        w, h = 300, 100
        r, g, b = 200, 100, 200
        for t in range(1, 1001, 10):
            line = "motion R "
            line += "{} {} {} {} {} {} {} {} {:.2f}   ".format(t, x, y, w, h, r, g, b, angle)
            angle += math.pi * math.sin(t) + random.random()
            line += "{} {} {} {} {} {} {} {} {:.2f}".format(t+10, x, y, w, h, r, g, b, angle)
            out.write(line + '\n')
    print("Done!")


def gen_combo_rotation(filename):
    CANVAS_ORIGIN = (0, 0)
    CANVAS_DIMS = (400, 400)
    with open(filename, 'w') as out:
        out.write("canvas {} {} {} {}\n".format(*CANVAS_ORIGIN, *CANVAS_DIMS))
        out.write("shape R rectangle\n")
        angle = 0
        x, y = 50, 150
        w, h = 300, 100
        r, g, b = 200, 100, 200
        for t in range(1, 1001, 10):
            line = "motion R "
            line += "{} {} {} {} {} {} {} {} {:.2f}   ".format(t, x, y, w, h, r, g, b, angle)
            w += int(50 * (random.random() - 0.5))
            h += int(50 * (random.random() - 0.5))
            r += 10
            r %= 255
            g -= 2
            g %= 255
            b += 5
            b %= 255
            x += int(50 * (random.random() - 0.5))
            y += int(50 * (random.random() - 0.5))
            angle += math.pi * math.sin(t) + random.random()
            line += "{} {} {} {} {} {} {} {} {:.2f}".format(t+10, x, y, w, h, r, g, b, angle)
            out.write(line + '\n')
    print("Done!")

gen_single_rotation("simple-geometry.txt")
gen_combo_rotation("combo-geometry.txt")

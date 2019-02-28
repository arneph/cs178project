# Script for joining the rows of Train and Test data (they seem to be separated
# for some reason)
# Usage: python joinrows.py [in_file] [out_file]

import sys

in_file = open(sys.argv[1], "r")
out_file = open(sys.argv[2], "w")

buf = ""
for i, line in enumerate(in_file):
    line = line.strip()
    if i == 0:
        out_file.write(f"{line}\n")
    elif i % 2 == 0:
        out_file.write(f"{buf}{line}\n")
    elif i % 2 == 1:
        buf = line

in_file.close()
out_file.close()

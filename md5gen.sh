#!/bin/bash

# Loop through each file in the current directory
for file in *; do
    # Check if the item is a file
    if [ -f "$file" ]; then
        # Generate the MD5 checksum for the file
        md5sum "$file" > "$file.md5"
        echo "Generated MD5 for $file"
    fi
done

echo "MD5 generation complete for all files"

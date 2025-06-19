# building
6/14/25


## Building

`./gradlew clean assemble`

## JVM

Artifacts are at

* `core/build/libs/netchdf-0.4.0.jar`

## linuxX64

Artifacts are at
* `core/build/bin/linuxX64/releaseShared/libnetchdf_api.h`
* `core/build/bin/linuxX64/releaseShared/libnetchdf.so`

### add library to LD_LIBRARY_PATH (or copy to existing) (or use current directory)

````
For example:

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/dev/core/build/bin/linuxX64/releaseShared

cp core/build/bin/linuxX64/releaseShared/libnetchdf.so /usr/local/lib

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
````

### create test c program

````
cd core/ctest
gcc main.c \
  -I/home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared \
  -L/home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared \
  -lnetchdf


./a.out \
  -L/home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared \
  -lnetchdf
````

### Example

````
~:$ echo $LD_LIBRARY_PATH
/usr/local/lib:/home/stormy/install/HDF_Group/HDF5/1.14.6/lib:.

cd core/ctest
cp /home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared/libnetchdf.so .

./a.out \
  -L/home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared \
  -lnetchdf

output is currently:

netchdf version 0.4.0
file simple_xy.nc
netcdf3 simple_xy.nc {
  dimensions:
    x = 6 ;
    y = 12 ;
  variables:
    int data(x, y) ;
}
nelems=72
first shape=-111406072
second shape=32190
 0 == -114171240
 1 == 32190
 2 == 1
 3 == 0
 4 == -1032136048
 5 == 22331
 6 == 124209
 7 == 0
 8 == 0
 9 == 0

````
### Example2

````
cd ~/dev/github/netcdf/netchdf/core/ctest
sudo cp /home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared/libnetchdf.so /usr/local/lib
gcc main.c   -I/home/stormy/dev/github/netcdf/netchdf/core/build/bin/linuxX64/debugShared   -lnetchdf
./a.out   -lnetchdf
````


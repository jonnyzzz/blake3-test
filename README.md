An experiment to test BLAKE3 performance and compare it with JVM. 
This is a hack-reporisoty for internal backup


Experiments on MacBook Pro (15-inch, 2019) Core i9:

```
(Collected the following sizes: from 2000, 6564100)
sha1   = 15143 ms
sha256 = 11186 ms
blake3 = 2376 ms              

blake3 vs SHA1 : 637%
blake3 vs SHA256 : 470%

```

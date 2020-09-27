# Implementation Plan

  - [ ] Use golang;
  - [ ] Continue [tutorial](https://golang.org/doc/tutorial/create-module);
  - [ ] Use encrypted [sqlite
        database](https://github.com/mutecomm/go-sqlcipher);
  - [ ] Use [GORM](https://gorm.io/) (hopefully with
        [go-sqlcipher](https://github.com/mutecomm/go-sqlcipher)?);
  - [ ] Integrate with [SSH
        agent](https://pkg.go.dev/golang.org/x/crypto/ssh/agent);  
        _(store database passphrase encrypted with SSH key)_
  - [ ] Write CLI;
  - [ ] Write D-Bus API;
  - [ ] Expand to UI (web interface - separate project, interface through
        D-Bus?).

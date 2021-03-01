
# Maintainer: Your Name <youremail@domain.com>
pkgname=st-patched
pkgver=0.8.4
pkgrel=1
pkgdesc="Simple virtual terminal emulator for X."
arch=(x86_64)
url="https://github.com/genghispirate/st-patched"
license=('MIT')
depends=(libxft)
makedepends=('ncurses' 'libxext' 'git')
provides=(package)
conflicts=(st)
replaces=(st)
source=("st-patched::git://github.com/genghispirate/st-patched")
md5sums=(SKIP)



pkgver() {
	cd "${_pkgname}"
	printf "%s.r%s.%s" "$(awk '/^VERSION =/ {print $3}' config.mk)" \
		"$(git rev-list --count HEAD)" "$(git rev-parse --short HEAD)"

}

prepare() {
	cd $srcdir/${_pkgname}
	# skip terminfo which conflicts with ncurses
	sed -i '/tic /d' Makefile

}

build() {
	cd "${_pkgname}"
	make X11INC=/usr/include/X11 X11LIB=/usr/lib/X11
}


package() {
	cd "${_pkgname}"
	make PREFIX=/usr DESTDIR="${pkgdir}" install
	install -Dm644 LICENSE "${pkgdir}/usr/share/licenses/${pkgname}/LICENSE"
	install -Dm644 README.md "${pkgdir}/usr/share/doc/${pkgname}/README.md"
	install -Dm644 Xdefaults "${pkgdir}/usr/share/doc/${pkgname}/Xdefaults.example"
}
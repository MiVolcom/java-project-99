run-dist:
	make -C run-dist

build:
	make -C app build

clean:
	make -C app clean

test:
	make -C app test

lint:
	make -C app lint
{ lib
, cmake
, gccStdenv
, graalvm-ce
, maven
}:

gccStdenv.mkDerivation rec {

  pname = "jgrapht-capi";

  version = "0.1.0";

  src = ./.;

  nativeBuildInputs = [ cmake graalvm-ce ];

  buildInputs = [ maven ];

  cmakeFlags = [ ];

  meta = with lib; {
    description = "JGraphT Library C-API";
    homepage = "https://github.com/jgrapht/jgrapht-capi";
    license = licenses.lgpl2;
    maintainers = [ maintainers.herberteuler ];
    platforms = platforms.linux;
  };
}

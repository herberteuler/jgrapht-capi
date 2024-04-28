{
  description = "JGraphT Library C-API";

  inputs = {
    flake-utils.url = "github:numtide/flake-utils";
    nixpkgs.url = "github:NixOS/nixpkgs/23.11";
  };

  outputs = { self, flake-utils, nixpkgs }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in {
        devShells.default = pkgs.mkShell rec {
          name = "jgrapht-capi";
          packages = with pkgs; [
            cmake
            gccStdenv
            graalvm-ce
            maven
          ];
          shellHook = let
            sgr0 = "\\[$(tput sgr0)\\]";
            hi-blue = "\\[\\e[1;34m\\]";
            hi-white = "\\[\\e[38;5;15m\\]";
            hi-yellow = "\\[\\e[38;5;228m\\]";
          in ''
            export EMACS_ENV="JGraphT C-API"
            export PS1="(${sgr0}${hi-white}${name}${sgr0}) [${sgr0}${hi-yellow}\w${sgr0}] ${sgr0}${hi-blue}\\$ ${sgr0}"
            unset TZ
          '';
        };
        packages.default = pkgs.callPackage ./default.nix {};
      }
    );
}

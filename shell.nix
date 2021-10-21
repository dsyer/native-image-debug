with import <nixpkgs> { };
mkShell {
  name = "env";
  buildInputs = [
    figlet
    gdb
  ];
  shellHook = ''
    figlet ":gdb:"
    gdb -v
  '';
}

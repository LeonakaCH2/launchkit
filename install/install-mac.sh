#!/usr/bin/env bash

set -e

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BIN_PATH="$PROJECT_ROOT/bin"
JAR_PATH="$PROJECT_ROOT/target/launchkit-0.1.0.jar"
LAUNCHER_PATH="$BIN_PATH/launchkit"

echo ""
echo "LaunchKit macOS/Linux Installer"
echo "Project root: $PROJECT_ROOT"
echo ""

if [ ! -f "$JAR_PATH" ]; then
  echo "LaunchKit jar not found."
  echo "Please build the project first:"
  echo "  mvn clean package"
  exit 1
fi

mkdir -p "$BIN_PATH"

cat > "$LAUNCHER_PATH" <<'EOF'
#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
java -jar "$SCRIPT_DIR/../target/launchkit-0.1.0.jar" "$@"
EOF

chmod +x "$LAUNCHER_PATH"

echo "Created launcher:"
echo "  $LAUNCHER_PATH"
echo ""

read -r -p "Do you want to add LaunchKit to your PATH? [Y/n] " answer

if [ "$answer" = "" ] || [ "$answer" = "y" ] || [ "$answer" = "Y" ] || [ "$answer" = "yes" ]; then
  SHELL_CONFIG=""

  if [ -n "$ZSH_VERSION" ]; then
    SHELL_CONFIG="$HOME/.zshrc"
  elif [ -n "$BASH_VERSION" ]; then
    SHELL_CONFIG="$HOME/.bashrc"
  else
    SHELL_CONFIG="$HOME/.profile"
  fi

  PATH_LINE="export PATH=\"$BIN_PATH:\$PATH\""

  if [ -f "$SHELL_CONFIG" ] && grep -Fxq "$PATH_LINE" "$SHELL_CONFIG"; then
    echo "LaunchKit is already in PATH."
  else
    echo "" >> "$SHELL_CONFIG"
    echo "# LaunchKit" >> "$SHELL_CONFIG"
    echo "$PATH_LINE" >> "$SHELL_CONFIG"
    echo "Added LaunchKit to PATH in:"
    echo "  $SHELL_CONFIG"
    echo ""
    echo "Please restart your terminal or run:"
    echo "  source $SHELL_CONFIG"
  fi
else
  echo "Skipped PATH update."
  echo "You can run LaunchKit with:"
  echo "  $LAUNCHER_PATH"
fi

echo ""
echo "Installation complete."
#!/bin/bash

# Base directory for migrations
BASE_MIGRATION_DIR="src/main/resources/db/changelog"

# Load environment variables from .env if it exists
if [ -f ".env" ]; then
  export $(grep -v '^#' .env | xargs)
fi

# Check if the base migration directory exists
if [ ! -d "$BASE_MIGRATION_DIR" ]; then
  echo "Migration directory $BASE_MIGRATION_DIR not found."
  exit 1
fi

# Scan available version directories
VERSIONS=($(ls -d "$BASE_MIGRATION_DIR"/*/ 2>/dev/null | xargs -n 1 basename))
if [ ${#VERSIONS[@]} -eq 0 ]; then
  echo "No version directories found in $BASE_MIGRATION_DIR."
  exit 1
fi

# Display available versions for selection
echo "Select a version for the migration:"
for i in "${!VERSIONS[@]}"; do
  echo "$((i + 1)). ${VERSIONS[i]}"
done

# Get the version number from the user
read -p "Enter the version number: " VERSION_NUMBER

# Check if the input is a valid number within the range of available versions
if ! [[ "$VERSION_NUMBER" =~ ^[0-9]+$ ]] || [ "$VERSION_NUMBER" -lt 1 ] || [ "$VERSION_NUMBER" -gt "${#VERSIONS[@]}" ]; then
  echo "Invalid version number."
  exit 1
fi

# Define the selected version
VERSION="${VERSIONS[VERSION_NUMBER-1]}"
MIGRATION_DIR="${BASE_MIGRATION_DIR}/${VERSION}"

# Get the current date and time in the required format
TIMESTAMP=$(date +"%Y-%m-%d-%H-%M-%S")

# Prompt for migration description
read -p "Enter a brief description of the migration (e.g., 'create-table-users'): " DESCRIPTION

# Determine the author of the migration
if [ -n "$LIQUIBASE_MIGRATION_AUTHOR" ]; then
  AUTHOR="$LIQUIBASE_MIGRATION_AUTHOR"
  echo "Migration author will be taken from the environment variable: $AUTHOR"
else
  read -p "Enter the name of the migration author: " AUTHOR
fi

# Create the migration file name
FILENAME="${MIGRATION_DIR}/${TIMESTAMP}-${DESCRIPTION}.yaml"

# Base template for the new migration file
cat <<EOF > "$FILENAME"
databaseChangeLog:
  - changeSet:
      id: ${TIMESTAMP}-${DESCRIPTION}
      author: ${AUTHOR}
      changes:
        -

      rollback:
        -
EOF

echo "Migration file created: $FILENAME"
